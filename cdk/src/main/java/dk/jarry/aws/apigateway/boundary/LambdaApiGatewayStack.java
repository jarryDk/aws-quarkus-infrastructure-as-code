package dk.jarry.aws.apigateway.boundary;

import dk.jarry.aws.apigateway.control.APIGatewayIntegrations;
import dk.jarry.aws.lambda.control.QuarkusLambda;
import software.amazon.awscdk.Stack;
import software.constructs.Construct;

public class LambdaApiGatewayStack extends Stack {

	/**
	 * <code>
	 * true -> integrates with Http API (quarkus-amazon-lambda-http) 
	 * false -> integrates with REST API (quarkus-amazon-lambda-rest)
	 * </code>
	 */
	final static boolean HTTP_API_GATEWAY_INTEGRATION = true;

	public LambdaApiGatewayStack(Construct app, String appName, String functionName) {
		this(app, appName, HTTP_API_GATEWAY_INTEGRATION, functionName);
	}

	public LambdaApiGatewayStack(Construct app, String appName, boolean httpApiGatewayIntegration,
			String functionName) {
		super(app, appName + "-apigateway-stack");
		var quarkuLambda = new QuarkusLambda(this, functionName);
		new APIGatewayIntegrations(this, httpApiGatewayIntegration, quarkuLambda.getFunction());
	}

}
