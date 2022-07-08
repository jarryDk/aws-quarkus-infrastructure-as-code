package dk.jarry.aws.lambda.control;

import java.util.List;
import java.util.Map;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.lambda.Architecture;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.IFunction;
import software.amazon.awscdk.services.lambda.ILayerVersion;
import software.amazon.awscdk.services.lambda.Runtime;
import software.constructs.Construct;

public class QuarkusLambda extends Construct {

	final static String LAMBDA_HANDLER = "io.quarkus.amazon.lambda.runtime.QuarkusStreamHandler::handleRequest";
	final static int MEMORY_SIZE = 1024; // ~0.5 vCPU
	final static int MAX_CONCURRENCY = 2;
	final static int TIMEOUT = 10;

	Map<String, String> configuration = Map.of("message", "Hello, quarkus as AWS Lambda");
	IRole lambdaRole = null;
	List<ILayerVersion> layers = null;
	IFunction function;

	public QuarkusLambda(Construct scope, String functionName) {
		this(scope, functionName, null, null, null);
	}
	
	public QuarkusLambda(Construct scope, String functionName, IRole lambdaRole) {
		this(scope, functionName, null, lambdaRole, null);
	}

	public QuarkusLambda(Construct scope, //
			String functionName, //
			Map<String, String> configuration, //
			IRole lambdaRole, //
			List<ILayerVersion> layers) {
		super(scope, "QuarkusLambda");
		if (configuration != null) {
			this.configuration = configuration;
		}
		this.lambdaRole = lambdaRole;
		if (layers != null) {
			this.layers = layers;
			this.function = createFunctionBasedOnLayer(functionName);
		} else {
			this.function = createFunction(functionName);
		}

	}

	IFunction createFunction(String functionName) {
		return Function.Builder.create(this, functionName) //
				.runtime(Runtime.JAVA_11) //
				.architecture(Architecture.ARM_64) //
				.code(Code.fromAsset("../lambda/target/function.zip")) //
				.handler(LAMBDA_HANDLER) //
				.memorySize(MEMORY_SIZE) //
				.functionName(functionName) //
				.environment(configuration) //
				.timeout(Duration.seconds(TIMEOUT)) //
				.role(lambdaRole) //
				.build();
	}

	IFunction createFunctionBasedOnLayer(String functionName) {
		return Function.Builder.create(this, functionName) //
				.runtime(Runtime.PROVIDED_AL2) //
				.layers(layers) //
				.code(Code.fromAsset("../lambda/target/function.zip")) //
				.handler(LAMBDA_HANDLER) //
				.memorySize(MEMORY_SIZE) //
				.functionName(functionName) //
				.environment(configuration) //
				.timeout(Duration.seconds(TIMEOUT)) //
				.reservedConcurrentExecutions(MAX_CONCURRENCY) //
				.role(lambdaRole) //
				.build();
	}

	public IFunction getFunction() {
		return this.function;
	}
}