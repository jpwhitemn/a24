package us.msrs.aurora24.model;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record UnscheduledJob(
		JobDefinition definition,
		UUID id,
		List<String> parameters,
		JobState jobState,
		String submitter,
		OffsetDateTime lastUpdated,
		OffsetDateTime startTime)
		implements Job {
}