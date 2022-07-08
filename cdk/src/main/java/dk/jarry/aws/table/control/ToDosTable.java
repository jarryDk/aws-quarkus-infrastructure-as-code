package dk.jarry.aws.table.control;

import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.dynamodb.ITable;

import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.BillingMode;

import software.constructs.Construct;

public class ToDosTable extends Construct {

	final static String TABLE_NAME = "todos";

	String appName;

	public ToDosTable(Construct scope, String appName) {
		super(scope, appName);
		this.appName = appName;
	}

	public ITable getTable() {
		return Table.fromTableName(this, appName + "-table", TABLE_NAME);
	}

	public Table createTable() {

		Table toDoTable = Table.Builder //
				.create(this, appName + "-" + TABLE_NAME + "-table").tableName(TABLE_NAME) //
				.partitionKey(Attribute.builder() //
						.name("uuid") //
						.type(AttributeType.STRING) //
						.build())
				// .replicationRegions(List.of("eu-central-1"))
				.readCapacity(1).writeCapacity(1).billingMode(BillingMode.PROVISIONED).build();

		return toDoTable;

	}
}
