package us.msrs.aurora24.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import us.msrs.aurora24.exception.NotFoundByID;
import us.msrs.aurora24.model.JobDefinition;

@Repository
public class JobDefinitionDao {

	private static List<JobDefinition> jobDefinitions;
	private List<String> temp = new ArrayList<String>();
	private List<String> temp2 = new ArrayList<String>();

	public List<JobDefinition> getJobDefinitions() {
		if (jobDefinitions == null) {
			jobDefinitions = new ArrayList<JobDefinition>();
			temp2.add("param1");
			temp2.add("param2");
			jobDefinitions.addAll(Arrays.asList(
					new JobDefinition("BeneficiaryChangeLetterJob", UUID.randomUUID(), temp, "Jim", true),
					new JobDefinition("SendRefundPayments", UUID.randomUUID(), temp2, "George", true),
					new JobDefinition("UpdateProjectionsJob", UUID.randomUUID(), temp, "Jim", true)));
		}
		return jobDefinitions;
	}

	public JobDefinition getByID(String id) {
		return getJobDefinitions().stream()
				.filter(job -> job.id().equals(UUID.fromString(id)))
				.findFirst()
				.orElse(null);
	}

	public JobDefinition getByName(String name) {
		return getJobDefinitions().stream().filter(job -> job.name().equals(name)).findFirst().orElse(null);
	}

	public JobDefinition createJobDefinition(String name, List<String> requiredParameters, String creator,
			boolean active) {
		if (jobDefinitions == null) {
			jobDefinitions = new ArrayList<JobDefinition>();
		}
		JobDefinition jobDef = new JobDefinition(name, UUID.randomUUID(), requiredParameters, creator, active);
		jobDefinitions.add(jobDef);
		return jobDef;
	}

	public boolean deleteJobDefinition(String id) {
		JobDefinition jobDefinition = getByID(id);
		if (jobDefinition != null) {
			jobDefinitions.remove(jobDefinition);
			return true;
		}
		return false;
	}

	public JobDefinition updateJobDefinition(String id, String name, List<String> requiredParameters, String creator,
			boolean active) throws NotFoundByID {
		JobDefinition jobDefinition = getByID(id);
		if (jobDefinition != null) {
			JobDefinition updatedJobDefinition = new JobDefinition(name, UUID.fromString(id), requiredParameters,
					creator, active);
			Collections.replaceAll(jobDefinitions, jobDefinition, updatedJobDefinition);
			return updatedJobDefinition;
		}

		throw new NotFoundByID("Job Definition not found with id: " + id);
	}
}
