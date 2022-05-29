# news-service

REST API aggregating news data

Swagger API docs `http://localhost:9081/swagger-ui/index.html`

### Stomp Websocket u

`ws://86.100.240.140:9081/news/websocket`

Subscribe to news feed:

* Subscribe `/user/topic/news`
* Send message `/app/queue/news`

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