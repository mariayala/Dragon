/*
 * hw3.c
 *
 *  Created on: Mar 10, 2013
 *      Author: lou
 */

#include "hw3.h"

/**
 * Loads an image (stored as an IplImage struct) for each
 * filename provided.
 * @param numImages		The number of images to load
 * @param filenames		A list of strings of the filenames to load
 */
IplImage** loadImages(int numImages, char ** fileNames) {
	IplImage** rv; // the return result
	// Allocate an array of numImages IplImage* s
	IplImage **s;
	s = (IplImage **) malloc (numImages*sizeof(IplImage *));
   
	int i = 0;
	int iscolor = -1; // Load the image without changing it
	// Iterate over each filename
	for(i ; i < numImages; i++){
      // printf("Filename is %s\n",fileNames[i]);
		// Load the image via the cvLoadImage function
		IplImage *loadedImg = cvLoadImage(fileNames[i], iscolor);
		// Check if the file could not be loaded.
		if(loadedImg == NULL){
			// if it could not load, print an error to stderr and return NULL
			fprintf(stderr, "Could not load image!\n");
			return NULL;			
		} else {
			// printf("Image loaded!\n");
			s[i] = loadedImg;
		}
	}
	// return results
	return s;
}

/**
 * Computes the distance between two colors (stored as CvScalars).
 *
 * Given a CvScalar c, you can access the blue, green, and red (BGR) elements
 * via c.val[0], c.val[1], and c.val[2], respectively.
 *
 * This function computes the distance between two colors as the euclidean
 * distance between the two BGR vectors.
 *
 * @see http://en.wikipedia.org/wiki/Euclidean_distance
 *
 * @param c1	The first color
 * @param c2	The second color
 * @returns		The euclidean distance between the two 3-d vectors
 */
double colorDistance(CvScalar c1, CvScalar c2) {
	double d = 0; // the result
	int i = 0; // an iterator
   int sum = 0; 
	double diff_b, diff_g, diff_r; // differences between each BGR value
	// iterate over the dimensions and compute the sum
   for(i ; i <3; i++){
		switch (i) {
		case 0:
			diff_b = c1.val[i]-c2.val[i];
			sum = sum + pow(diff_b,2.0);
			break;
		case 1:
			diff_g = c1.val[i]-c2.val[i];
			sum = sum + pow(diff_g,2.0);
			break;
		case 2:
			diff_r = c1.val[i]-c2.val[i];
			sum = sum + pow(diff_r,2.0);
			break;
		default:
			// Not doing anything
			break;
		}
	}
	// return the square root of the result.
	d = sqrt(sum);
	// If d is zero, just return 0.
	if( d == 0 ){
		return 0;
	}
	return d;
}

/**
 * Splits up an image into numColumns by numRows sub-images and returns the results.
 *
 * @param src	The source image to split
 * @param numColumns	The number of columns to split into
 * @param numRows 		The number of rows to split into
 * @returns				A numColumns x numRows array of IplImages
 */
IplImage ** getSubImages(IplImage* src, int numColumns, int numRows) {
	int cellWidth, cellHeight, y, x, i;
	IplImage ** rv;
	CvSize s = cvGetSize(src);

	// Compute the cell width and the cell height
	cellWidth = s.width / numColumns;
	cellHeight = s.height / numRows;

	// Allocate an array of IplImage* s
	rv = malloc(sizeof(IplImage*) * numColumns * numRows);
	if (rv == NULL) {
		fprintf(stderr, "Could not allocate rv array\n");
	}

	// Iterate over the cells
	i = 0;
	for (y = 0; y < s.height; y += cellHeight)
		for (x = 0; x < s.width; x += cellWidth) {
			// Create a new image of size cellWidth x cellHeight and
			// store it in rv[i]. The depth and number of channels should
			// be the same as src.
			rv[i] = cvCreateImage(cvSize(cellWidth, cellHeight), src->depth,
					src->nChannels);
			if (rv[i] == NULL) {
				fprintf(stderr, "Could not allocate image %d\n", i);
			}

			// set the ROI of the src image
			cvSetImageROI(src, cvRect(x, y, cellWidth, cellHeight));

			// copy src to rv[i] using cvCopy, which obeys ROI
			cvCopy(src, rv[i], NULL);

			// reset the src image roi
			cvResetImageROI(src);

			// increment i
			i++;
		}

	// return the result
	return rv;
}

/**
 * Finds the CvScalar in colors closest to t using the colorDistance function.
 * @param t		 	The color to look for
 * @param scolors	The colors to look through
 * @param numColors	The length of scolors
 * @returns			The index of scolors that t is closest to
 * 					(i.e., colorDistance( t, scolors[result]) <=
 * 					colorDistance( t, scolors[i]) for all i != result)
 */
int findClosest(CvScalar t, CvScalar* scolors, int numColors) {
	int rv = 0, // return value
		 i = 0; // used to iterate
	double d, // stores the result of distance
	       m = colorDistance(t, scolors[0]); // the current minimum distance
	// iterate over scolors
		for(i ; i < numColors; i++){
			// compute the distance between t and scolors[i]
			d = colorDistance(t, scolors[i]);
			// check if the distance is less then current minimum
				if( d <= m){
					// if it is, store i as the current result and cache the minimum distance
					rv = i; 
					m = d;
				}
	}
	// return result.
	return rv;
}

/**
 * For each image provided, computes the average color vector
 * (represented as a CvScalar object).
 *
 * @param images	The images
 * @param numImages	The length of images
 * @returns 		An numImages length array of CvScalars where rv[i] is the average color in images[i]
 */
CvScalar* getAvgColors(IplImage** images, int numImages) {
	CvScalar* rv;
	int i = 0;
	// create return vector
	rv = malloc(numImages*sizeof(CvScalar));
	CvScalar my_rv;

	// iterate over images and compute average color
	for(i; i < numImages; i++){
		// for each image, compute the average color (hint: use cvAvg)
		const CvArr *arr = images[i];
		const CvArr *mask = NULL;
		my_rv = cvAvg(arr,mask);
		rv[i] = my_rv;
	}
	// return result
	return rv;
}

/**
 * Given an ordered list of images (iclosest), creates a
 * numColumns x numRows grid in a new image, copies each image in, and returns the result.
 *
 * Thus, if numColumns is 10, numRows is 5, and each iclosest image is 64x64, the resulting image
 * would be 640x320 pixels.
 *
 * @param iclostest		A numColumns x numRows list of images in row-major order to be put into the resulting image.
 * @param numColumns  	Number of horizontal cells
 * @param numRows		Number of vertical cells
 */
IplImage* stitchImages(IplImage** iclosest, int numColumns, int numRows) {
	int j = 0, // for iterating over the rows
	    i = 0; // for iterating over the columns
	printf("Starting stitch\n");

	// TODO: using cvGetSize, get the size of the first image in iclosest.
	// remember all of the images should be the same size
	const CvArr* arr = iclosest[i];
	CvSize firstSize = cvGetSize(arr);

	// TODO: Compute the size of the final destination image.
	int height = firstSize.height; 
	int width = firstSize.width;
	int fheight = numRows*height;
	int fwidth = numColumns*width;

	CvSize finalSize = cvSize(fwidth,fheight);
   printf("Initial image is %dx%d\n", height, width);
	printf("Final %dx%d\n", finalSize.height, finalSize.width); 
	// TODO: allocate the return image. This can be potentially large, so
	// you should make sure the result is not null

	// TODO: iterate over each cell and copy the closest image into it

			// TODO: set the ROI of the result

			// TODO: copy the proper image into the result

			// TODO: reset the ROI of the result

	// TODO: return the result

}
