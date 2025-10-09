package com.ilway.skystat.framework.common.data;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.time.ZonedDateTime;

@MappedSuperclass
@Getter
public class BaseTimeData {

	@Column(name = "created_at", insertable = false, updatable = false)
	private ZonedDateTime createdAt;

	@Column(name = "updated_at", insertable = false, updatable = false)
	private ZonedDateTime updatedAt;

}
