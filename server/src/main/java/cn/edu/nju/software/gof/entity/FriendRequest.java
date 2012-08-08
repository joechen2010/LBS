package cn.edu.nju.software.gof.entity;

import java.io.Serializable;

public class FriendRequest  implements Serializable {
	
    private static final long serialVersionUID = -6832688344486748908L;

	private Long id;

	private Long targetPersonId;

	private Long sourcePersonId;

	public FriendRequest() {
		super();
	}

	public FriendRequest(Long targetPersonId, Long sourcePersonId) {
		super();
		this.targetPersonId = targetPersonId;
		this.sourcePersonId = sourcePersonId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTargetPersonId() {
		return targetPersonId;
	}

	public void setTargetPersonId(Long targetPersonId) {
		this.targetPersonId = targetPersonId;
	}

	public Long getSourcePersonId() {
		return sourcePersonId;
	}

	public void setSourcePersonId(Long sourcePersonId) {
		this.sourcePersonId = sourcePersonId;
	}


}
