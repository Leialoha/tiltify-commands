Action Calls
============

Action calls are used to execute multiple actions at once.

For example, if you want perks to stack depending on your donation levels, a simple way would to add action calls to your actions **OR** copy and paste commands over and over again...

Here is how to do it:

```
tiltify:
  ...
  actions:
    MyFirstAction:
      type: ONCE
      amount: 10
      text:
        - "Run this action but don't include MySecondAction"
        - "This will include MyThirdAction however"
	  call:
	    - MyThirdAction
    MySecondAction:
      type: ONCE
      amount: 1
      text:
        - "This action will only excute if a user donates between 1 and 5 dollars"
    MyThirdAction:
      amount: 0
      text:
        - "This action will execute anytime MyFirstAction is executed"
```

Notice that `MyThirdAction` does not have a `type` parameter.
This will make `MyThirdAction` only callable with the `call` parameter!