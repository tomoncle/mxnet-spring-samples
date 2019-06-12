#!/bin/sh

# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
set -e

if [[ "$(uname)" == "Darwin" ]]; then # Mac OS X
    echo "Max OS"
elif [[ "$(expr substr $(uname -s) 1 5)" == "Linux" ]]; then # GNU/Linux
    echo "Linux"
elif [[ "$(expr substr $(uname -s) 1 5)" == "MINGW" ]]; then # Windows
    echo "Windows"
fi

USER_HOME=$HOME
MXNET_ROOT=${USER_HOME}/mxnet-models

if [[ ! -d ${MXNET_ROOT} ]]; then
    mkdir ${MXNET_ROOT}
fi

data_path=${MXNET_ROOT}/resnet50_ssd

image_path=${MXNET_ROOT}/resnet50_ssd/images

if [[ ! -d ${data_path} ]]; then
    mkdir -p ${data_path}
fi

wget https://s3.amazonaws.com/model-server/models/resnet50_ssd/resnet50_ssd_model-symbol.json -P $data_path
wget https://s3.amazonaws.com/model-server/models/resnet50_ssd/resnet50_ssd_model-0000.params -P $data_path
wget https://s3.amazonaws.com/model-server/models/resnet50_ssd/synset.txt -P $data_path

if [[ ! -d ${image_path} ]]; then
   mkdir -p ${image_path}
fi

cd ${image_path}
wget https://cloud.githubusercontent.com/assets/3307514/20012567/cbb60336-a27d-11e6-93ff-cbc3f09f5c9e.jpg -O dog.jpg
wget https://cloud.githubusercontent.com/assets/3307514/20012563/cbb41382-a27d-11e6-92a9-18dab4fd1ad3.jpg -O person.jpg

cd ${MXNET_ROOT}

