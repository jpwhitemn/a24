package us.msrs.aurora24.model;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class UnscheduledJob extends Job {

	private OffsetDateTime startTime;

	public OffsetDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(OffsetDateTime startTime) {
		this.startTime = startTime;
	}

	public UnscheduledJob(JobDefinition definition, UUID id, List<String> parameters, JobState jobState,
			String submitter, OffsetDateTime lastUpdated, OffsetDateTime startTime) {
		super(definition, id, parameters, jobState, submitter, lastUpdated);
		this.startTime = startTime;
	}

}