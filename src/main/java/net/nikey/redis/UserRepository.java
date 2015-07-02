/*
 * Copyright 2011 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.nikey.redis;

import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.data.redis.support.collections.*;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

/**
 * �û����ݲֿ�
 * @author arvin
 */
@Named
public class UserRepository {

	/**
	 * redis String ���ݲ���ģ��
	 * String-focused extension of RedisTemplate.
	 */
	private final StringRedisTemplate template;

	/**
	 * �û�������
	 * Atomic long backed by Redis. Uses Redis atomic increment/decrement and watch/multi/exec operations for CAS
	 * operations.
	 */
	private final RedisAtomicLong userIdCounter;

	/**
	 * Redis operations for simple (or in Redis terminology 'string') values.
	 */
	private final ValueOperations<String, String> valueOps;

	/**
	 * global users
	 */
	private RedisList<String> users;

	/**
	 * ע��xml���õ�ģ��
	 * @param template
	 */
	@Inject
	public UserRepository(StringRedisTemplate template) {
		this.template = template;
		valueOps = template.opsForValue();
		//list����
		users = new DefaultRedisList<String>(KeyUtils2.users(), template);
		//
		userIdCounter = new RedisAtomicLong(KeyUtils2.globalUid(), template.getConnectionFactory());
	}


	/**
	 * ��֤�û��Ƿ��Ѿ�����
	 * @param name ������
	 * @return
	 */
	public boolean isUserValid(String name) {
		return template.hasKey(KeyUtils2.user(name));
	}

	/**
	 * ����û�
	 * @param name ������
	 * @param password ������
	 * @return
	 */
	public String addUser(String name, String password) {
		//����long����
		String uid = String.valueOf(userIdCounter.incrementAndGet());
		// save user as hash
		//KeyUtils2.uid(uid) ��ʽ��"uid:1"
		BoundHashOperations<String, String, String> userOps = template.boundHashOps(KeyUtils2.uid(uid));
		userOps.put("name", name);
		userOps.put("pass", password);

		//KeyUtils2.user(uid) ��ʽ�� "user:001@163.com:uid"
		valueOps.set(KeyUtils2.user(name), uid);

		//�������user
		users.addFirst(name);

		return addAuth(name);
	}

	/**
	 * �������Ʋ�ѯ����
	 * @param name
	 * @return
	 */
	public String findUid(String name) {
		return valueOps.get(KeyUtils2.user(name));
	}

	/**
	 * �����֤
	 * @param name
	 * @return
	 */
	public String addAuth(String name) {
		String uid = findUid(name);
		// add random auth key relation
		String auth = UUID.randomUUID().toString();
		valueOps.set(KeyUtils2.auth(uid), auth);
		valueOps.set(KeyUtils2.authKey(auth), uid);
		return auth;
	}

	/**
	 *  �û���֤
	 * @param user �� �û�
	 * @param pass �� ����
	 * @return
	 */
	public boolean auth(String user, String pass) {
		// find uid
		String uid = findUid(user);
		if (StringUtils.hasText(uid)) {
			BoundHashOperations<String, String, String> userOps = template.boundHashOps(KeyUtils2.uid(uid));
			return userOps.get("pass").equals(pass);
		}

		return false;
	}

	/**
	 * �û���֤
	 * @param value
	 * @return
	 */
	public String findNameForAuth(String value) {
		String uid = valueOps.get(KeyUtils2.authKey(value));
		return findName(uid);
	}

	/**
	 * �û���ѯ
	 * @param uid
	 * @return
	 */
	private String findName(String uid) {
		if (!StringUtils.hasText(uid)) {
			return "";
		}
		BoundHashOperations<String, String, String> userOps = template.boundHashOps(KeyUtils2.uid(uid));
		return userOps.get("name");
	}
}