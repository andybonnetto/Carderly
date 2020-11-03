import cv2
# import sys
import numpy as np
# numpy.set_printoptions(threshold=sys.maxsize)

PATH= "testimage.png"
img = cv2.imread(PATH,cv2.IMREAD_GRAYSCALE)


# harris = cv2.cornerHarris(img,2,3,0.04)
# print(harris)
# mask = np.zeros_like(img)
# mask[harris>0.01*harris.max()] = 255
# cam_quit = 0
# while not cam_quit:
#     key = cv2.waitKey(1) & 0xFF
#     if key == ord("q"):
#         cam_quit = 1
#     cv2.imshow('mask', mask)
#     cv2.imshow("img",img)

kernel_size = 15
blur_gray = cv2.GaussianBlur(img,(kernel_size, kernel_size),0)
low_threshold = 50
high_threshold = 100
edges = cv2.Canny(blur_gray, low_threshold, high_threshold)
rho = 1  # distance resolution in pixels of the Hough grid
theta = np.pi / 180  # angular resolution in radians of the Hough grid
threshold = 15  # minimum number of votes (intersections in Hough grid cell)
min_line_length = 50  # minimum number of pixels making up a line
max_line_gap = 20  # maximum gap in pixels between connectable line segments
line_image = np.copy(img) * 0  # creating a blank to draw lines on

# Run Hough on edge detected image
# Output "lines" is an array containing endpoints of detected line segments
lines = cv2.HoughLinesP(edges, rho, theta, threshold, np.array([]),
                    min_line_length, max_line_gap)

for line in lines:
    for x1,y1,x2,y2 in line:
        cv2.line(line_image,(x1,y1),(x2,y2),(255,0,0),5)

# Draw the lines on the  image
lines_edges = cv2.addWeighted(img, 0.8, line_image, 1, 0)
cam_quit = 0
while not cam_quit:
    key = cv2.waitKey(1) & 0xFF
    if key == ord("q"):
        cam_quit = 1
    cv2.imshow('mask', lines_edges)
    cv2.imshow("img",blur_gray)

# M = cv.moments(cnt)
# print( M )