package com.ilway.skystat.framework.adapter.output.mysql;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.port.output.MetarManagementOutputPort;
import com.ilway.skystat.domain.vo.metar.Metar;
import com.ilway.skystat.framework.adapter.output.mysql.data.MetarData;
import com.ilway.skystat.framework.adapter.output.mysql.mapper.MetarMySQLMapper;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarManagementRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
public class MetarManagementMySQLAdapter implements MetarManagementOutputPort {

	private static final int CHUNK_SIZE = 1_000;
	private final MetarManagementRepository repository;
	private final EntityManager em;

	@Override
	@Transactional
	public void save(Metar metar) {
		repository.save(MetarMySQLMapper.metarDomainToData(metar));
	}

	@Override
	@Transactional
	public void saveAll(List<Metar> metars) {
		for (int from = 0; from < metars.size(); from+=CHUNK_SIZE) {
			int to = Math.min(from+CHUNK_SIZE, metars.size());

			List<MetarData> batch = metars.subList(from, to).stream()
				                            .map(MetarMySQLMapper::metarDomainToData)
				                            .toList();

			repository.saveAll(batch);
			em.flush();
			em.clear();
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Metar> findAllByIcao(String icao) {
		List<Long> ids = repository.findIdsByIcaoSorted(icao);
		return retrieveMetarsInChunks(ids);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Metar> findByIcaoAndPeriod(String icao, RetrievalPeriod period) {
		List<Long> ids = repository.findIdsByIcaoAndPeriod(icao, period.fromInclusive(), period.toEsclusive());
		return retrieveMetarsInChunks(ids);
	}

	private List<Metar> retrieveMetarsInChunks(List<Long> ids) {
		List<Metar> result = new ArrayList<>(ids.size());

		for (int from = 0; from< ids.size(); from+=CHUNK_SIZE) {
			int to = Math.min(from+CHUNK_SIZE, ids.size());
			List<Long> chunkIds = ids.subList(from, to);

			Map<Long, Integer> order = new HashMap<>();
			for (int i = 0; i < chunkIds.size(); i++) {
				order.put(chunkIds.get(i), i);
			}

			List<MetarData> chunk = repository.findAllById(chunkIds);
			chunk.forEach(m -> {
				m.getClouds().size();
				m.getWeathers().size();

				m.getWeathers().forEach(w -> {
					w.getPhenomena().size();
					w.getDescriptors().size();
				});
			});

			chunk.sort(Comparator.comparing(md -> order.get(md.getId())));
			List<Metar> chunkData = chunk.stream()
				                        .map(MetarMySQLMapper::metarDataToDomain)
				                        .toList();
			result.addAll(chunkData);
			em.clear();
		}

		return result;
	}


}
