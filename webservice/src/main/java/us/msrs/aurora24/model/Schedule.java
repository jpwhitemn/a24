package us.msrs.aurora24.model;

import java.util.UUID;

public record Schedule(
		String name,
		UUID id,
		String creator,
		boolean active,
		String cron) {

}
