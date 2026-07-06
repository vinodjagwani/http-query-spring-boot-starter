# http-query-spring-boot-starter

Adds support for the HTTP `QUERY` method ([RFC 10008](https://datatracker.ietf.org/doc/rfc10008/)) to Spring Boot 4.1 servlet applications. `RequestMethod.QUERY` doesn't exist in the framework yet, so this starter fills the gap with `@HttpQueryMapping`, wired in through Boot's own auto-configuration mechanism.

Requirements: Spring Boot 4.1.x on the servlet stack (Spring MVC), Java 21+.

## Step 1: Add the dependency

Not published to a repository yet, so build and install it to your local Maven cache first:

```bash
git clone <this-repo>
cd http-query-spring-boot-starter
mvn install
```

Then add it to any Spring Boot project's `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot.httpquery</groupId>
    <artifactId>http-query-spring-boot-starter</artifactId>
    <version>0.1.0</version>
</dependency>
```

That's it. No properties to set, no `@Import`, no `WebMvcConfigurer` to write. It registers automatically the same way any other Boot auto-configuration module does.

## Step 2: Use it in your Spring Boot project

Annotate a handler method with `@HttpQueryMapping` exactly like you'd use `@GetMapping` or `@PostMapping`:

```java
import org.springframework.boot.httpquery.HttpQueryMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @HttpQueryMapping(value = "/search", consumes = "application/json", produces = "application/json")
    public List<Item> search(@RequestBody SearchCriteria criteria) {
        return itemRepository.findMatching(criteria);
    }
}
```

It coexists freely with `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping` in the same controller, with or without a class-level `@RequestMapping`. Nothing else in your app needs to change.

Call it with any HTTP client that supports custom methods:

```bash
curl -X QUERY http://localhost:8080/api/items/search \
  -H "Content-Type: application/json" \
  -d '{"category":"hardware","keyword":"Widget"}'
```

## Caveats

- Requires a servlet container that passes an arbitrary method token through instead of rejecting it at the connector level. Verified on embedded Tomcat (bundled with Boot 4.1): Tomcat validates methods against the generic HTTP token grammar, not a fixed whitelist, so this works out of the box.
- `QUERY` is not a CORS-safelisted method. If this is browser-facing, add it to your own `WebMvcConfigurer.addCorsMappings(...)` allowed methods.
- A wrong method (e.g. `POST`) against a `@HttpQueryMapping`-only path returns `404`, not the usual `405`, since the custom condition backing this doesn't plug into Spring's built-in methods condition the way a real `RequestMethod` enum constant would.
- This is a stopgap until `RequestMethod.QUERY` ships in Spring Framework itself. Once it does, this starter becomes unnecessary.
