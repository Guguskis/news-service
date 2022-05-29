# news-service

REST API aggregating news data

Swagger API docs `http://localhost:9081/swagger-ui/index.html`

### Stomp Websocket

`ws://86.100.240.140:9081/news/websocket`

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
  "channel": "REDDIT",
  "sentiment": {
    "id": 1,
    "sentiment": "NEGATIVE",
    "scoreNegative": 0.77,
    "scorePositive": 0.02,
    "scoreNeutral": 0.2
  }
}
```
