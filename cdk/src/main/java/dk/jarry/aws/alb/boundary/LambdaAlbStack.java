package dk.jarry.aws.alb.boundary;

import java.util.List;

import dk.jarry.aws.alb.control.Alb;
import dk.jarry.aws.alb.control.PublicVPC;
import dk.jarry.aws.lambda.control.QuarkusLambda;
import dk.jarry.aws.role.control.ToDoLambdaRole;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.elasticloadbalancingv2.AddApplicationTargetsProps;
import software.amazon.awscdk.services.elasticloadbalancingv2.BaseApplicationListenerProps;
import software.amazon.awscdk.services.elasticloadbalancingv2.HealthCheck;
import software.amazon.awscdk.services.elasticloadbalancingv2.targets.LambdaTarget;
import software.amazon.awscdk.services.iam.IRole;
import software.constructs.Construct;

public class LambdaAlbStack extends Stack {

	public LambdaAlbStack(Construct app, String appName, String functionName) {

		super(app, appName + "-alb-stack");

		var publicVPCConstruct = new PublicVPC(this);
		var publicVPC = publicVPCConstruct.getVpc();
		var alb = new Alb(this, publicVPC, appName + "-elb");

		var loadBalancer = alb.getApplicationLoadBalancer();
		var listener = loadBalancer.addListener(appName + "-http-listener", BaseApplicationListenerProps.builder() //
				.port(80) //
				.build());

		ToDoLambdaRole toDoLambdaRole = new ToDoLambdaRole(this, appName);
		IRole role = toDoLambdaRole.createRole();
				
		var quarkuLambda = new QuarkusLambda(this, functionName, role);
		var function = quarkuLambda.getFunction();
		
		var lambdaTarget = new LambdaTarget(function);

		listener.addTargets(appName + "lambda-target", //
				AddApplicationTargetsProps.builder() //
						.targets(List.of(lambdaTarget)) //
						.healthCheck(HealthCheck.builder() //
								.enabled(true) //
								.build()) //
						.build());

		var url = loadBalancer.getLoadBalancerDnsName();

		CfnOutput.Builder.create(this, "FunctionARN") //
				.value(function.getFunctionArn()).build();
		CfnOutput.Builder.create(this, "LoadBalancerDNSName") //
				.value(url).build();
		CfnOutput.Builder.create(this, "LoadBalancerCurlOutput") //
				.value("curl -i http://" + url + "/hello").build();
	}
}
