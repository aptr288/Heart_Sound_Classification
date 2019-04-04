# MLHeartData
The CSV files used for implementing machine learning algorithms 

The NewTr, NewTes and NewVal files represents latest files, which contain more number of normal and different disease files.

The data is also split into train test and validation to make sure evaluation and parameter tuning
done properly to make the model perform well on unseen data

The old data which I used initially was very small in size so couldnt even split them into validation and test,
so the test file was used for both evaluating the model and parameter tuning.

FinalTrain file is combination of train and validation files which is tested on NewTes file for final evaluation. 
