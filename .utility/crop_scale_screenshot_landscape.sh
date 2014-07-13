#!/bin/bash
#
# Script to remove top and side bar from an Android screenshot in landscape orientation.
#
# usage : ./crop_scale_screenshot_landscape.sh image.png
#
# Dependency : ImageMagick
#
# Copyright (C) 2014 Dieter Adriaenssens
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.

imageFile=$1

convert -size 1920x1080 -extract 1792x1005+0+75 $imageFile -resize 713x400 $imageFile
