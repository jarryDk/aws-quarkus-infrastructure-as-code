package dk.jarry.aws.functionurl.boundary;

import dk.jarry.aws.lambda.control.QuarkusLambda;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.lambda.FunctionUrlAuthType;
import software.amazon.awscdk.services.lambda.FunctionUrlOptions;
import software.constructs.Construct;

public class FunctionURLStack extends Stack {

	public FunctionURLStack(Construct app, String appName, String functionName) {
		super(app, appName + "-function-url-stack");

		var quarkuLambda = new QuarkusLambda(this, functionName);
		var function = quarkuLambda.getFunction();
		var functionUrl = function.addFunctionUrl(FunctionUrlOptions.builder() //
				.authType(FunctionUrlAuthType.NONE).build());
		String url = functionUrl.getUrl();

		CfnOutput.Builder.create(this, "FunctionURLOutput") //
				.value(url).build();
		CfnOutput.Builder.create(this, "FunctionCurlOutput") //
				.value("curl -i " + url + "hello").build();
	}

}
