from behave import given, when, then
import nose.tools as nt

# from ....src.aissemble_test_data_delivery_pyspark_model.schema.city_schema import (
#     CitySchema,
# )

from aissemble_test_data_delivery_pyspark_model.schema.city_schema import (
    CitySchema,
)


@given('the record "City" exists with the following relations')
def step_impl(context):
    # Records and relations are handled in MDA generation
    nt.assert_true(CitySchema() is not None, "City Schema was not generated correctly")


@given('the spark schema is generate for the "City" record')
def step_impl(context):
    context.schema = CitySchema()


@given('the spark schema is generate for the "PersonWithMToOneRelation" record')
def step_impl(context):
    context.schema = CitySchema()


@given("a city record is created")
def step_impl(context):
    raise NotImplementedError("STEP: And a city record is created")


@given('the spark schema is generate for the "PersonWithOneToOneRelation" record')
def step_impl(context):
    raise NotImplementedError(
        'STEP: Given the spark schema is generate for the "PersonWithOneToOneRelation" record'
    )


@given('a "{validity}" "PersonWithOneToOneRelation" dataSet exists')
def step_impl(context, validity):
    raise NotImplementedError(
        'STEP: And a "<validity>" "PersonWithOneToOneRelation" dataSet exists'
    )


@given('a "{validity}" "PersonWithMToOneRelation" dataSet exists')
def step_impl(context, validity):
    raise NotImplementedError(
        'STEP: And a "<validity>" "PersonWithMToOneRelation" dataSet exists'
    )


@when('a "City" dict is mapped to a spark dataset using the record')
def step_impl(context):
    raise NotImplementedError(
        'STEP: When a "City" dict is mapped to a spark dataset using the record'
    )


@when('spark schema validation is performed on the "PersonWithMToOneRelation" dataSet')
def step_impl(context):
    raise NotImplementedError(
        'STEP: When spark schema validation is performed on the "PersonWithMToOneRelation" dataSet'
    )


@when(
    'spark schema validation is performed on the "PersonWithOneToOneRelation" dataSet'
)
def step_impl(context):
    raise NotImplementedError(
        'STEP: When spark schema validation is performed on the "PersonWithOneToOneRelation" dataSet'
    )


@then('the schema data type for "{record}" is "{type}"')
def step_impl(context, record, type):
    nt.assert_equal(str(context.schema.get_data_type(record.upper())), type)


@then("the dataset has the correct values for the relational objects")
def step_impl(context):
    raise NotImplementedError(
        "STEP: Then the dataset has the correct values for the relational objects"
    )


@then('the dataSet validation "{success}"')
def step_impl(context, success):
    raise NotImplementedError('STEP: Then the dataSet validation "<success>"')
