package edu.uc.labs.heartbeat.tasks;

import edu.uc.labs.heartbeat.service.HeartbeatService;
import edu.uc.labs.heartbeat.service.RabbitService;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class RabbitCleanupTask implements ScheduledTask {

    private static final Logger log = LoggerFactory.getLogger(RabbitCleanupTask.class);

    @Async
    @Override
    public void run() {
        rabbitCleanupThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                HttpGet httpget = new HttpGet("http://iris.uc.edu:15672/api/queues/%2fheartbeat");

                // Create a custom response handler
                ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                    public String handleResponse(
                            final HttpResponse response) throws ClientProtocolException, IOException {
                        int status = response.getStatusLine().getStatusCode();
                        if (status >= 200 && status < 300) {
                            HttpEntity entity = response.getEntity();
                            return entity != null ? EntityUtils.toString(entity) : null;
                        } else {
                            throw new ClientProtocolException("Unexpected response status: " + status);
                        }
                    }
                };


                try {
                    String response = httpClient.execute(httpget,responseHandler);
                    
                } catch (IOException e) {
                }

            }
        });
    }
    @Autowired
    ThreadPoolTaskExecutor rabbitCleanupThreadPool;
    @Autowired
    HeartbeatService heartbeatService;
    @Autowired
    RabbitService rabbitService;
    @Autowired
    HttpClient httpClient;
}
