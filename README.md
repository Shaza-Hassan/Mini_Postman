# Mini_Postman
This App is using Asynctask to make get/post request

## The Following Pattern
We used MVVM pattern while developing
The separation of the code in MVVM is divided into View, ViewModel and Model:
- View is the collection of visible elements, which also receives user input. This includes user interfaces (UI), animations and text. The content of View is not interacted with directly to change
what is presented.
- ViewModel is located between the View and Model layers. This is where the controls for interacting with View are housed, while binding is used to connect the UI elements in View to the controls in
ViewModel.
- Model houses the logic for the program, which is retrieved by the ViewModel upon its own receipt of input from the user through View. In the model we follow **Repo Pattern** to handle getting data

## The App contains five pagakes
- history package -> responsable for get cached request and appling needed filter
- home pacakge -> responsable for show first screen for the where you could needed data to make calling api
- response pacage -> responsable for show the result of request
- shared -> contains models that used by different packages
- utils
  - contains async task that responsable for calling api
  - contains calling for sqlite to store and get request
 
