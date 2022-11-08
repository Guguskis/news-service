[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=Guguskis_TodoApp&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=Guguskis_TodoApp)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Guguskis_TodoApp&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=Guguskis_TodoApp)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=Guguskis_TodoApp&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=Guguskis_TodoApp)

[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=Guguskis_TodoApp&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=Guguskis_TodoApp)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=Guguskis_TodoApp&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=Guguskis_TodoApp)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=Guguskis_TodoApp&metric=bugs)](https://sonarcloud.io/summary/new_code?id=Guguskis_TodoApp)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=Guguskis_TodoApp&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=Guguskis_TodoApp)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=Guguskis_TodoApp&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=Guguskis_TodoApp)

# news-service

REST API aggregating news data

Swagger API docs `http://localhost:9081/swagger-ui/index.html`

### Stomp Websocket

`ws://localhost:9081/news/websocket`

Subscribe to news feed:

* Subscribe `/user/topic/news`
* Send message `/app/queue/news`

#### Request:

```json
{
  "action": "SUBSCRIBE",
  "channel": "REDDIT",
  "subChannels": [
    "EyeBleach",
    "explainlikeimfive",
    "funny"
  ]
}
```

#### Response:

```json
{
  "id": 1,
  "title": "She is gonna take away jobs",
  "url": "https://old.reddit.com/r/funny/comments/v06dct/she_is_gonna_take_away_jobs/",
  "created": "2022-05-29T17:22:18Z",
  "subChannel": "funny",
  "channel": "REDDIT"
}
```

## Architecture

![Hexagonal architecture](resources/architecture.png)

Project's hexagonal architecture (see picture above) is based on domain driven design. At the core **UseCase** operates 
on **Entity** completely decoupling business logic from technical details, aka **Adapter**. 