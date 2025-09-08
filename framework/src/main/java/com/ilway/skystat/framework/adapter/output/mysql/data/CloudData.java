package com.ilway.skystat.framework.adapter.output.mysql.data;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Table(name = "metar_cloud")
public class CloudData {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private Long id;

	@Setter
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "metar_id")
	private MetarData metar;

	@Column(name = "coverage")
	private String coverage;

	@Column(name = "altitude_ft")
	private int altitude;

	@Column(name = "cloud_type")
	private String cloudType;



}
