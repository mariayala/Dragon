Name : Maria A Yala
Assignment : cs283 OpenCv Mosaic

_-------------------------------------------------_
                OpenCv Mosaic
_                                                 _
 -------------------------------------------------
 
OpenCv Mosaic is a program that takes in an image file 
and used other images , creating thumbnails from them, 
to generate a mosaic version of the original image.


----------------------------------------------------
                 What works
----------------------------------------------------
I had a tricky time with the stitching but i finally 
got it working when I figured out that I had forgotten
to set back the incrementor for the nested loop back to 
zero. So my inner loop only ran twice and I ended up with 
a final image that had only two rows coloured and the rest 
were black. However the program works as expected now.

Note: Additional documetation can be seen in my code 
comments in the modified methods

