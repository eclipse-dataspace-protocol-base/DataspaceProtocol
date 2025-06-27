# Introduction

This directory contains the Schema Table Generator plugin. The plugin generates readable type information in HTML for JSON
schema definitions found in the project source set.

For example:

```html

<table class="message-table">
    <tr>
        <td class="message-class" colspan="3">MessageOffer</td>
    </tr>
    <tr>
        <td class="message-properties-heading" colspan="3">Required properties</td>
    </tr>
    <tr>
        <td class="code">@id</td>
        <td>string</td>
        <td></td>
    </tr>
    <tr>
        <td class="code">@type</td>
        <td>string</td>
        <td>Value must be <span class="code">Offer</span></td>
    </tr>
    <tr>
        <td class="message-properties-heading" colspan="3">Optional properties</td>
    </tr>
    <tr>
        <td class="code">obligation</td>
        <td>array</td>
        <td></td>
    </tr>
    <tr>
        <td class="code">rule</td>
        <td>array</td>
        <td></td>
    </tr>
    <tr>
        <td class="code">profile</td>
        <td>any</td>
        <td></td>
    </tr>
</table>
```

For each type, a generated HTML table is created and output to `<build dir>/generated/tables`. File names are the
typename in lowercase with an `.html` suffix. These files may then be imported into the ReSpec-based documentation using
the `aside` element in the appropriate Markdown file:

```html

<aside data-include="generated/tables/messageoffer.html"></aside>
```
   
## Implementation notes

The JSON Schema Object Model parser does not yet support all JSON Schema features such `anyOf` or `oneOf`.  

# Build Setup

The plugin can be applied and configured as follows:

```kotlin
apply<SchemaTableGeneratorPlugin>();

configure<SchemaTableGeneratorPluginExtension> {
    schemaPrefix = "https://w3id.org/dspace/2025/1/"
    schemaFileSuffix = "-schema.json"
}
```

The `schemPrefix` property is used to specify the base URL for resolving schema references. This base URL will be mapped
relative to where the schema files reside on the local filesystem. The `schemaFileSuffix` propery is used to filter
schema files to include. 

# Running

The generation process can be run by specifying the `generateTablesFromSchemas` task:

```
./gradlew generateTablesFromSchemas
```

To debug the generation process, use:

```
./gradlew -Dorg.gradle.debug=true --no-daemon generateTablesFromSchemas
```

