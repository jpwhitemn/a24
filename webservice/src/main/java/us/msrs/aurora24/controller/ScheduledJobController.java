package us.msrs.aurora24.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import us.msrs.aurora24.MQTTCommandPublisher;
import us.msrs.aurora24.dao.JobDefinitionDao;
import us.msrs.aurora24.dao.ScheduleDao;
import us.msrs.aurora24.dao.ScheduledJobDao;
import us.msrs.aurora24.exception.NotFoundByID;
import us.msrs.aurora24.exception.OperationNotAllowed;
import us.msrs.aurora24.model.Command;
import us.msrs.aurora24.model.JobDefinition;
import us.msrs.aurora24.model.JobState;
import us.msrs.aurora24.model.Schedule;
import us.msrs.aurora24.model.ScheduledJob;

@Controller
public class ScheduledJobController {

	@Autowired
	private ScheduledJobDao dao;

	@Autowired
	ScheduleDao scheduleDao;

	@Autowired
	JobDefinitionDao jobDefinitionDao;
	
	@Autowired
	MQTTCommandPublisher publisher;

	@QueryMapping
	public List<ScheduledJob> scheduledJobs() {
		return dao.getJobs();
	}

	@QueryMapping
	public List<ScheduledJob> scheduledJobsByState(@Argument JobState jobState) {
		return dao.getByState(jobState);
	}

	@QueryMapping
	public ScheduledJob scheduledJobById(@Argument String id) {
		return dao.getById(id);
	}

	@QueryMapping
	public ScheduledJob scheduledJobCommandRequestById(@Argument String id, @Argument Command command)
			throws NotFoundByID, OperationNotAllowed {
		ScheduledJob job = dao.getById(id);
		// #TODO what would change in the state and timestamps of the job? JobState =
		// Submitted on start? lastupdate changed?
		if (command == Command.Start) {
			throw new OperationNotAllowed("Scheduled jobs are not started manually.  Change the schedule");
		}
		if (job == null)
			throw new NotFoundByID("No job with id: " + id);

		boolean pub_success = publisher.publishMessgae(id, command);
		System.out.println(String.format("Queuing job %s to %s", id, command));
		if (!pub_success) {
			System.out.println("Queue of job failed");
			// #TODO reset job to not started, etc.
			// #TODO log
			return null;
		}
		System.out.println("Queued successfully");
		// #TODO log publishing
		return job;
	}

	@MutationMapping
	public ScheduledJob createScheduledJob(@Argument String jobDefinitionName, @Argument List<String> parameters,
			@Argument JobState jobState, @Argument String submitter, @Argument String scheduleName) {
		// #TODO - deal with issues where job def or schedule are not found
		JobDefinition jobDef = jobDefinitionDao.getByName(jobDefinitionName);
		Schedule schedule = scheduleDao.getByName(scheduleName);
		return dao.createScheduledJob(jobDef, parameters, jobState, submitter, schedule);
	}

	@MutationMapping
	public boolean deleteScheduledJob(@Argument String id) {
		return dao.deleteScheduledJob(id);
	}

	@MutationMapping
	public ScheduledJob updateScheduledJob(@Argument String id, @Argument String jobDefinitionName,
			@Argument List<String> parameters,
			@Argument JobState jobState, @Argument String submitter, @Argument String scheduleName)
			throws NotFoundByID {
		// #TODO - deal with issues where job def or schedule are not found
		JobDefinition jobDef = jobDefinitionDao.getByName(jobDefinitionName);
		Schedule schedule = scheduleDao.getByName(scheduleName);
		return dao.updateScheduledJob(id, jobDef, parameters, jobState, submitter, schedule);
	}

}
