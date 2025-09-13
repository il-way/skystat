package com.ilway.skystat.framework.adapter.output.mysql.data;

import com.ilway.skystat.domain.vo.weather.type.CloudCoverage;
import com.ilway.skystat.domain.vo.weather.type.CloudType;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.EnumType.STRING;
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
	@Enumerated(STRING)
	private CloudCoverage coverage;

	@Column(name = "altitude_ft")
	private Integer altitude;

	@Column(name = "cloud_type")
	@Enumerated(STRING)
	private CloudType cloudType;

}
