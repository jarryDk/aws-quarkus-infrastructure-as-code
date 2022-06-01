package dk.jarry.alb.boundary;

import java.util.List;

import dk.jarry.alb.control.Alb;
import dk.jarry.alb.control.PublicVPC;
import dk.jarry.lambda.control.QuarkusLambda;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.elasticloadbalancingv2.AddApplicationTargetsProps;
import software.amazon.awscdk.services.elasticloadbalancingv2.BaseApplicationListenerProps;
import software.amazon.awscdk.services.elasticloadbalancingv2.HealthCheck;
import software.amazon.awscdk.services.elasticloadbalancingv2.targets.LambdaTarget;
import software.constructs.Construct;

public class LambdaAlbStack extends Stack {

	public LambdaAlbStack(Construct scope, String id, String functionName) {
		super(scope, id + "-alb-stack");
		
		var publicVPCConstruct = new PublicVPC(this);
		var publicVPC = publicVPCConstruct.getVpc();
		var alb = new Alb(this, publicVPC, "DkJarryLambdaLB");

		var loadBalancer = alb.getApplicationLoadBalancer();
		var listener = loadBalancer.addListener("Http", BaseApplicationListenerProps.builder() //
				.port(80) //
				.build());

		var quarkuLambda = new QuarkusLambda(this, functionName);
		var function = quarkuLambda.getFunction();
		var lambdaTarget = new LambdaTarget(function);

		listener.addTargets("Lambda", //
			AddApplicationTargetsProps.builder() //
				.targets(List.of(lambdaTarget)) //
				.healthCheck(HealthCheck.builder() //
						.enabled(true) //
						.build()) //
				.build());

		CfnOutput.Builder.create(this, "FunctionARN").value(function.getFunctionArn()).build();
		var url = loadBalancer.getLoadBalancerDnsName();
		CfnOutput.Builder.create(this, "LoadBalancerDNSName") //
			.value(url).build();
		CfnOutput.Builder.create(this, "LoadBalancerCurlOutput") //
			.value("curl -i http://" + url + "/hello").build();
	}
}
