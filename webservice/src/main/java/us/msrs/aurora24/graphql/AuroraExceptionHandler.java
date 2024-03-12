package us.msrs.aurora24.graphql;

import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import us.msrs.aurora24.exception.NotFoundByID;
import us.msrs.aurora24.exception.OperationNotAllowed;

@Component
public class AuroraExceptionHandler extends DataFetcherExceptionResolverAdapter {

	@Override
	protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
		if (ex instanceof NotFoundByID) {
			return GraphqlErrorBuilder.newError()
					.errorType(ErrorType.NOT_FOUND)
					.message(ex.getMessage())
					.path(env.getExecutionStepInfo().getPath())
					.location(env.getField().getSourceLocation())
					.build();
		}
		if (ex instanceof OperationNotAllowed) {
			return GraphqlErrorBuilder.newError()
					.errorType(ErrorType.NOT_FOUND)
					.message(ex.getMessage())
					.path(env.getExecutionStepInfo().getPath())
					.location(env.getField().getSourceLocation())
					.build();
		} else {
			return null;
		}
	}
}
