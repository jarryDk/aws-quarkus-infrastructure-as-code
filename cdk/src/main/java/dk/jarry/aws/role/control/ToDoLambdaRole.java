package dk.jarry.aws.role.control;

import software.amazon.awscdk.services.iam.Role;

import java.util.List;

import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.constructs.Construct;

public class ToDoLambdaRole extends Construct {

	String ROLE_NAME = "todo-lambda-role";

	String appName;

	public ToDoLambdaRole(Construct scope, String appName) {
		super(scope, appName);
		this.appName = appName;
	}

	public IRole getRole() {
		return Role.fromRoleName(this, appName + "-role", ROLE_NAME);
	}

	public IRole createRole() {

		Role lambdaRole = Role.Builder.create(this, "ToDo-Lambda-Role") //
				.assumedBy(new ServicePrincipal("lambda.amazonaws.com")) //
				.description("ToDo Lambda Role ... with policy to use DynamoDb and Logs") //
				.roleName(ROLE_NAME) //
				.build();

		lambdaRole.addToPolicy( //
				PolicyStatement.Builder.create().actions( //
						List.of( //
								"dynamodb:DescribeTable", //
								"dynamodb:ListTables", //
								"dynamodb:PutItem", //
								"dynamodb:GetItem", //
								"dynamodb:UpdateItem", //
								"dynamodb:DeleteItem", //
								"dynamodb:Scan", //
								"logs:CreateLogGroup", //
								"logs:CreateLogStream", //
								"logs:PutLogEvents") //
				) //
						.resources(List.of("*")) //
						.build());

		return lambdaRole;
	}

}
