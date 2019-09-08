package com.shan.app.domain;

import java.io.Serializable;
import java.util.Objects;

public class ManagerAuthorityId implements Serializable {
	
	private Long manager;
	private Long authority;
	
	public ManagerAuthorityId() { }
	public ManagerAuthorityId(Long manager, Long authority) {
		this.manager = manager;
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
		ManagerAuthorityId that = (ManagerAuthorityId) o;
		return Objects.equals(manager, that.manager) &&
				Objects.equals(authority, that.authority);
	}

	@Override
	public int hashCode() {

		return Objects.hash(manager, authority);
	}
}
