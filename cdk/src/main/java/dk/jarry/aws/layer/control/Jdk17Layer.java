package dk.jarry.aws.layer.control;

import java.util.Arrays;

import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.LayerVersion;
import software.amazon.awscdk.services.lambda.ILayerVersion;
import software.amazon.awscdk.services.lambda.LayerVersionProps;
import software.amazon.awscdk.services.lambda.Runtime;
import software.constructs.Construct;

public class Jdk17Layer extends Construct {

	final static String NAME = "java17layer";

	String appName;

	public Jdk17Layer(Construct scope, String appName) {
		super(scope, appName);
		this.appName = appName;
	}

	public ILayerVersion getLayer(String account, String version) {
		String layerVersionArn = "arn:aws:lambda:eu-central-1:" + account + ":layer:" + NAME + ":" + version;
		return LayerVersion.fromLayerVersionArn(this, NAME, layerVersionArn);
	}

	public ILayerVersion createLayer() {
		return new LayerVersion(this, NAME, //
				LayerVersionProps.builder() //
						.layerVersionName(NAME) //
						.description("Java 17") //
						.compatibleRuntimes(Arrays.asList(Runtime.PROVIDED_AL2)) //
						.code(Code.fromAsset("../java17layer/java17layer.zip")) //
						.build());
	}

}
