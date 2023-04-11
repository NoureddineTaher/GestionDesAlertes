package fr.real.supervision.appliinfo.web.diffusiongroup.service;

import java.util.List;

import fr.real.supervision.appliinfo.model.Alert;
import fr.real.supervision.appliinfo.model.DiffusionGroup;
import fr.real.supervision.appliinfo.web.diffusiongroup.dto.DiffusionGroupDto;

public interface DiffusionGroupService {

	List<DiffusionGroup> getDiffusionGroups();

	DiffusionGroup getDiffusionGroupById(Long id);

	void deleteDiffusionGroupById(Long id);

	List<Alert> getAlertsForThisDiffusionGroup(Long id);

	void saveDiffusionGroup(DiffusionGroupDto diffusionGroup);

	void updateDiffusionGroup(DiffusionGroupDto diffusionGroupDto);

	DiffusionGroupDto getDiffusionGroupDtoById(Long id);

}
