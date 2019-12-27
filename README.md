# RSS-Feed
A beautiful RSS feed app

### Update:

* The app uses MVVM (View Model) design pattern.
* Using MVVM makes the code much cleaner and robust respecting the lifecycle of the activities.
* LiveData is stored in the FeedViewModel class so the data is not downloaded again when the orientation is changed so the scroll position is also maintained.
* The Observer (MainActivity) receives the event of updated feed list from the FeedCustomAdapter.
