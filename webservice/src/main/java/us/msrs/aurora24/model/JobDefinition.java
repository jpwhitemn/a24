package us.msrs.aurora24.model;

import java.util.List;
import java.util.UUID;

public record JobDefinition(

		String name,
		UUID id,
		List<String> requiredParameters,
		String creator,
		boolean active) {
}
