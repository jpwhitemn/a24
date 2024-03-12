package us.msrs.aurora24.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import us.msrs.aurora24.dao.JobDefinitionDao;
import us.msrs.aurora24.dao.UnscheduledJobDao;
import us.msrs.aurora24.exception.NotFoundByID;
import us.msrs.aurora24.model.Command;
import us.msrs.aurora24.model.JobDefinition;
import us.msrs.aurora24.model.JobState;
import us.msrs.aurora24.model.UnscheduledJob;

@Controller
public class UnscheduledJobController {

	@Autowired
	private UnscheduledJobDao dao;

	@Autowired
	JobDefinitionDao jobDefinitionDao;

	@QueryMapping
	public List<UnscheduledJob> unscheduledJobs() {
		return dao.getJobs();
	}

	@QueryMapping
	public List<UnscheduledJob> unscheduledJobsByState(@Argument JobState jobState) {
		return dao.getByState(jobState);
	}

	@QueryMapping
	public UnscheduledJob unscheduledJobById(@Argument String id) {
		return dao.getById(id);
	}

	@QueryMapping
	public UnscheduledJob unscheduledJobCommandRequestById(@Argument String id, @Argument Command command ) throws NotFoundByID {
		UnscheduledJob job = dao.getById(id);
		// #TODO what would change in the state and timestamps of the job? JobState = Submitted on start?  lastupdate changed?
		if (job == null)
			throw new NotFoundByID("No job with id: " + id);
		System.out.println("Requesting " + command + " of job: "+ job.id());
		return job;
	}
	
	@MutationMapping
	public UnscheduledJob createUnscheduledJob(@Argument String jobDefinitionName, @Argument List<String> parameters,
			@Argument JobState jobState, @Argument String submitter) {
		// #TODO - deal with issues where job def is not found
		JobDefinition jobDef = jobDefinitionDao.getByName(jobDefinitionName);
		return dao.createUnscheduledJob(jobDef, parameters, jobState, submitter);
	}

	@MutationMapping
	public boolean deleteUnscheduledJob(@Argument String id) {
		return dao.deleteUnscheduledJob(id);
	}

	@MutationMapping
	public UnscheduledJob updateUnscheduledJob(@Argument String id, @Argument String jobDefinitionName,
			@Argument List<String> parameters,
			@Argument JobState jobState, @Argument String submitter, @Argument String scheduleName) throws NotFoundByID {
		// #TODO - deal with issues where job def is not found
		JobDefinition jobDef = jobDefinitionDao.getByName(jobDefinitionName);
		return dao.updateUnscheduledJob(id, jobDef, parameters, jobState, submitter);
	}

}
