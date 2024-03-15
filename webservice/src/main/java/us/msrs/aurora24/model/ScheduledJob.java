package us.msrs.aurora24.model;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class ScheduledJob extends Job {

	private Schedule schedule;

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public ScheduledJob(JobDefinition definition, UUID id, List<String> parameters, JobState jobState, String submitter,
			OffsetDateTime lastUpdated, Schedule schedule) {
		super(definition, id, parameters, jobState, submitter, lastUpdated);
		this.schedule = schedule;
	}

}
