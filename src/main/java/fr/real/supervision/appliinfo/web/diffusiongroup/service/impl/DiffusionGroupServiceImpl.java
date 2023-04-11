package fr.real.supervision.appliinfo.web.diffusiongroup.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.real.supervision.appliinfo.model.Alert;
import fr.real.supervision.appliinfo.model.DiffusionGroup;
import fr.real.supervision.appliinfo.repository.AlertRepository;
import fr.real.supervision.appliinfo.repository.DiffusionGroupRepository;
import fr.real.supervision.appliinfo.web.diffusiongroup.dto.DiffusionGroupDto;
import fr.real.supervision.appliinfo.web.diffusiongroup.service.DiffusionGroupService;
import fr.real.supervision.appliinfo.web.util.AuthenticationUser;

@Service
public class DiffusionGroupServiceImpl implements DiffusionGroupService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DiffusionGroupServiceImpl.class);

	@Autowired
	private DiffusionGroupRepository diffusionGroupRepository;

	@Autowired
	private AlertRepository alertRepository;

	@Override
	public List<DiffusionGroup> getDiffusionGroups() {
		Iterable<DiffusionGroup> findAll = diffusionGroupRepository.findAll();
		List<DiffusionGroup> collect = StreamSupport.stream(findAll.spliterator(), false).collect(Collectors.toList());
		collect.sort((c1, c2) -> c1.getName().compareTo(c2.getName()));
		return collect;
	}

	@Override
	public DiffusionGroup getDiffusionGroupById(Long id) {
		return diffusionGroupRepository.findById(id).orElse(null);
	}

	@Override
	public DiffusionGroupDto getDiffusionGroupDtoById(Long id) {
		DiffusionGroup diffusionGroup = getDiffusionGroupById(id);
		if (diffusionGroup == null) {
			return null;
		} else {
			return populateDtoFromDiffusionGroup(diffusionGroup);
		}
	}

	@Override
	public void deleteDiffusionGroupById(Long id) {
		diffusionGroupRepository.deleteById(id);
	}

	@Override
	public void saveDiffusionGroup(DiffusionGroupDto diffusionGroupDto) {
		DiffusionGroup diffusionGroup = populateDiffusionGroupFromDto(diffusionGroupDto);
		diffusionGroup.setCreatedBy(AuthenticationUser.getAuthenticatedUser());
		diffusionGroupRepository.save(diffusionGroup);
		LOGGER.info("Creation diffusion {}  par {} le {} ", diffusionGroup.getName(), diffusionGroup.getCreatedBy(),
				diffusionGroup.getCreatedDate());
	}

	@Override
	public void updateDiffusionGroup(DiffusionGroupDto diffusionGroupDto) {
		DiffusionGroup diffusionGroup = populateDiffusionGroupFromDto(diffusionGroupDto);
		diffusionGroup.setLastModifiedBy(AuthenticationUser.getAuthenticatedUser());
		diffusionGroup.setLastModifiedDate(LocalDateTime.now());
		diffusionGroupRepository.save(diffusionGroup);
		LOGGER.info("Modification diffusion {} par {} le {}", diffusionGroup.getName(),
				diffusionGroup.getLastModifiedBy(), diffusionGroup.getLastModifiedDate());
	}

	@Override
	public List<Alert> getAlertsForThisDiffusionGroup(Long id) {
		return alertRepository.findByDiffusionGroupsId(id);
	}

	private DiffusionGroup populateDiffusionGroupFromDto(DiffusionGroupDto diffusionGroupDto) {
		DiffusionGroup diffusionGroup = new DiffusionGroup();
		diffusionGroup.setId(diffusionGroupDto.getId());
		diffusionGroup.setName(diffusionGroupDto.getName());
		diffusionGroup.setDescription(diffusionGroupDto.getDescription());
		diffusionGroup.setContacts(diffusionGroupDto.getContacts());
		return diffusionGroup;
	}

	private DiffusionGroupDto populateDtoFromDiffusionGroup(DiffusionGroup diffusionGroup) {
		DiffusionGroupDto diffusionGroupDto = new DiffusionGroupDto();
		diffusionGroupDto.setId(diffusionGroup.getId());
		diffusionGroupDto.setName(diffusionGroup.getName());
		diffusionGroupDto.setDescription(diffusionGroup.getDescription());
		diffusionGroupDto.setContacts(diffusionGroup.getContacts());
		return diffusionGroupDto;
	}

}
