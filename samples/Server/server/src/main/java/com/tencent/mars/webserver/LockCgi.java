/*
* Tencent is pleased to support the open source community by making Mars available.
* Copyright (C) 2016 THL A29 Limited, a Tencent company. All rights reserved.
*
* Licensed under the MIT License (the "License"); you may not use this file except in 
* compliance with the License. You may obtain a copy of the License at
* http://opensource.org/licenses/MIT
*
* Unless required by applicable law or agreed to in writing, software distributed under the License is
* distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
* either express or implied. See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.tencent.mars.webserver;

import com.tencent.mars.sample.chat.proto.Lock;
import com.tencent.mars.utils.LogUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import org.apache.log4j.Logger;

@Path("/mars/getLock")
public class LockCgi {

    Logger logger = Logger.getLogger(LockCgi.class.getName());

    @POST()
    @Consumes("application/octet-stream")
    @Produces("application/octet-stream")
    public Response hello(InputStream is) {
        try {
            final Lock.GetLockRequest request = Lock.GetLockRequest.parseFrom(is);

            logger.info(LogUtils.format("request from sn=%s, address=%s", request.getSn(), request.getAddress()));

            final Lock.LockResponse response = Lock.LockResponse.newBuilder()
                                                                 .setBoxCode(1)
                                                                 .setBoxSize(Lock.LockResponse.BoxSize.LARGE)
                                                                 .setCabinetCode(1)
                                                                 .setIsOpen(true)
                                                                 .setRequest(request)
                                                                 .build();

            final StreamingOutput stream = new StreamingOutput() {
                public void write(OutputStream os) throws IOException {
                    response.writeTo(os);
                }
            };
            return Response.ok(stream).build();

        } catch (Exception e) {
            logger.info(LogUtils.format("%s", e));
        }

        return null;
    }
}