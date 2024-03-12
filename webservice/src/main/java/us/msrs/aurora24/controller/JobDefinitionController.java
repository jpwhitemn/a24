package us.msrs.aurora24.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import us.msrs.aurora24.dao.JobDefinitionDao;
import us.msrs.aurora24.exception.NotFoundByID;
import us.msrs.aurora24.model.JobDefinition;

@Controller
public class JobDefinitionController {

	@Autowired
	private JobDefinitionDao dao;

	@QueryMapping
	public List<JobDefinition> jobDefinitions() {
		return dao.getJobDefinitions();
	}

	@QueryMapping
	public JobDefinition jobDefinitionById(@Argument String id) {
		return dao.getByID(id);
	}

	@QueryMapping
	public JobDefinition jobDefinitionByName(@Argument String name) {
		return dao.getByName(name);
	}

	@MutationMapping
	public JobDefinition createJobDefinition(@Argument String name, @Argument List<String> requiredParameters,
			@Argument String creator, @Argument boolean active) {
		return dao.createJobDefinition(name, requiredParameters, creator, active);
	}

	@MutationMapping
	public boolean deleteJobDefinition(@Argument String id) {
		return dao.deleteJobDefinition(id);
	}

	@MutationMapping
	public JobDefinition updateJobDefinition(@Argument String id, @Argument String name,
			@Argument List<String> requiredParameters, @Argument String creator,
			@Argument boolean active) throws NotFoundByID {
		return dao.updateJobDefinition(id, name, requiredParameters, creator, active);
	}

}
