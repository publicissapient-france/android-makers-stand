# Android Makers Stand

## Simon

[Guideline](https://docs.google.com/presentation/d/1tLJ2PW7Q7T12ithRcHKtkafyTgvN5T6JVElZZzVzyk0/)

### New Game
- A player, clicking on white button
- Store in Firebase:

```
player/

{
  id: '<unique id>',
  name: '<random name>',
  count: <number of color found>,
  start: <timestamp at start>
}
```

### End of Game
- A player, loosing round
- Store in Firebase:

```
score/

[
 ...
 {
  id: <player id>
  name: '<player name>',
  count: <number of color found>,
  time: <elapsed time since beginning in milliseconds>
 }
]
```

- Remove `/player` node

Example of `JSON`:

```
{
	"player": {
		"id": "f64f2940-fae4-11e7-8c5f-ef356f279131",
		"name": "arnaud",
		"count": 10,
		"start": 1520606607
	},
	"score": [{
		"id": "f64f2940-fae4-11e7-8c5f-ef356f279131",
		"name": "arnaud",
		"count": 10,
		"time": 60000
	}, {
		"id": "f64f2940-fae4-11e7-8c5f-ef356f279132",
		"name": "michael",
		"count": 10,
		"time": 50000
	}]
}
```
