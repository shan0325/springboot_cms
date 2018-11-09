package com.shan.app.domain;

import java.io.Serializable;
import java.util.Objects;

public class UserAuthorityId implements Serializable {
	
	private String user;
	private String authority;
	
	public UserAuthorityId() { }
	public UserAuthorityId(String user, String authority) {
		this.user = user;
		this.authority = authority;
	}
	
	// equals, hashcode 구현
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		UserAuthorityId that = (UserAuthorityId) o;
		return Objects.equals(user, that.user) &&
				Objects.equals(authority, that.authority);
	}

	@Override
	public int hashCode() {

		return Objects.hash(user, authority);
	}
}
