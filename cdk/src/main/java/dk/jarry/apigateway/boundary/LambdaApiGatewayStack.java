package dk.jarry.apigateway.boundary;

import dk.jarry.apigateway.control.APIGatewayIntegrations;
import dk.jarry.lambda.control.QuarkusLambda;
import software.amazon.awscdk.Stack;
import software.constructs.Construct;

public class LambdaApiGatewayStack extends Stack {

	// true -> integrates with Http API, false -> integrates with REST API
	static boolean HTTP_API_GATEWAY_INTEGRATION = true;

	public LambdaApiGatewayStack(Construct scope, String id, String functionName) {
		this(scope, id, HTTP_API_GATEWAY_INTEGRATION, functionName);
	}

	public LambdaApiGatewayStack(Construct scope, String id, boolean httpApiGatewayIntegration, String functionName) {
		super(scope, id + "-apigateway-stack");
		var quarkuLambda = new QuarkusLambda(this, functionName);
		new APIGatewayIntegrations(this, httpApiGatewayIntegration, quarkuLambda.getFunction());
	}

}
