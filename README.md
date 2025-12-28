# LocationTrackerApp

Project architecture : 

I used proper MVVM architecture for this location tracker project. There is separate packages for all types of files.

How Offline Storage works : 

I used room database for offline storage. For example if there is no internet connectivity then all saved user logs will be stored locally.

How sync mechanism works :

When customer is saving their locations in the app and if there is no internet connection then all locations will be stored locally to one module called " unSync logs " and will be sync to firebase instantly while getting internet connectivity.


API contract used for sending locations :

I used firebase Real time database for storing user's location. It is a simple firebase Real time database connectivity app. That will store all locations to firebase if there is internet connectivity.

Assumptions and limitations : 

If there is 10 user's logs in the unSync module and when internet comes it will sync automatically then sometimes it is not storing in sequence.
