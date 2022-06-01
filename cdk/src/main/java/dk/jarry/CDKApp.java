package dk.jarry;

import dk.jarry.alb.boundary.LambdaAlbStack;
import dk.jarry.apigateway.boundary.LambdaApiGatewayStack;
import dk.jarry.functionurl.boundary.FunctionURLStack;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.Tags;

public class CDKApp {

	public static String FUNCTION_NAME = "dk_jarry_lambda_gretings_boundary_Greetings";
	
	public static void main(final String[] args) {

		var app = new App();
		var appName = "quarkus-lambda";

		Tags.of(app).add("project", "MicroProfile with Quarkus on AWS Lambda");
		Tags.of(app).add("environment", "development");
		Tags.of(app).add("application", appName);

		var stackProps = createStackProperties();
		
		/**
		 * Use quarkus-amazon-lambda-http in ../lambda/pom.xml
		 */
		// new FunctionURLStack(app, appName, FUNCTION_NAME);

		/**
		 * Use quarkus-amazon-lambda-http in ../lambda/pom.xml if APIGatewayIntegrations Http API
		 * 
		 * Use quarkus-amazon-lambda-rest in ../lambda/pom.xml if APIGatewayIntegrations REST API
		 * 
		 */
		// new LambdaApiGatewayStack(app, appName, FUNCTION_NAME);

		/**
		 * Use quarkus-amazon-lambda-rest in ../lambda/pom.xml
		 */
		new LambdaAlbStack(app,appName, FUNCTION_NAME);

		app.synth();
	}

	static StackProps createStackProperties() {
		return StackProps.builder().build();
	}

	static StackProps createStackPropertiesFromEnv() {
		var account = System.getenv("CDK_DEPLOY_ACCOUNT");
		var region = System.getenv("CDK_DEPLOY_REGION");

		if (account == null)
			return StackProps.builder().build();

		var environment = Environment.builder().account(account).region(region).build();
		return StackProps.builder().env(environment).build();
	}
}
