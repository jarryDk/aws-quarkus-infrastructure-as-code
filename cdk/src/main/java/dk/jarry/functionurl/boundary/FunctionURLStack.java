package dk.jarry.functionurl.boundary;

import dk.jarry.lambda.control.QuarkusLambda;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.lambda.FunctionUrlAuthType;
import software.amazon.awscdk.services.lambda.FunctionUrlOptions;
import software.constructs.Construct;

public class FunctionURLStack extends Stack {

	public FunctionURLStack(Construct construct, String id, String functionName) {
		super(construct, id + "-function-url-stack");
		var quarkuLambda = new QuarkusLambda(this, functionName);
		var function = quarkuLambda.getFunction();
		var functionUrl = function.addFunctionUrl(FunctionUrlOptions.builder() //
				.authType(FunctionUrlAuthType.NONE).build());
		CfnOutput.Builder.create(this, "FunctionURLOutput") //
				.value(functionUrl.getUrl()).build();
	}
	
}
