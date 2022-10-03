Action Type
===========

There are 2 different action types:

- TOTAL - Run this action when a collective amount dollars is reached.

- ONCE - Runs everytime a donation is made. Will ignore smaller donation levels, for example if a user donates $100 and a $10 action exists, it will run that action unless there is another action that higher than $10.

If you do not provide an action type, it will make it only accessible from action calls.

```
tiltify:
  ...
  actions:
    randomActionName:
      type: [Insert action type]
	  ...
```
