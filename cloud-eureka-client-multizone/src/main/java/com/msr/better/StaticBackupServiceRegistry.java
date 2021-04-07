package com.msr.better;

import com.netflix.appinfo.DataCenterInfo;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.MyDataCenterInfo;
import com.netflix.discovery.BackupRegistry;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;
import org.springframework.stereotype.Component;

/**
 * 注册中心服务端
 *
 * @author MaiShuRen
 * @site http://www.maishuren.top
 * @date 2020-10-23
 */
@Component("staticBackupServiceRegistry")
public class StaticBackupServiceRegistry implements BackupRegistry {

    private Applications localApps = new Applications();

    public StaticBackupServiceRegistry() {
        Application application = new Application("org");
        InstanceInfo instanceInfo1 = InstanceInfo.Builder.newBuilder()
                .setAppName("org-service")
                .setVIPAddress("org-service")
                .setInstanceId("org-instance-1")
                .setHostName("192.168.79.100")
                .setIPAddr("192.168.79.100")
                .setPort(9090)
                .setDataCenterInfo(new MyDataCenterInfo(DataCenterInfo.Name.MyOwn))
                .setStatus(InstanceInfo.InstanceStatus.UP)
                .build();

        InstanceInfo instanceInfo2 = InstanceInfo.Builder.newBuilder()
                .setAppName("org-service")
                .setVIPAddress("org-service")
                .setInstanceId("org-instance-1")
                .setHostName("192.168.79.100")
                .setIPAddr("192.168.79.100")
                .setPort(9091)
                .setDataCenterInfo(new MyDataCenterInfo(DataCenterInfo.Name.MyOwn))
                .setStatus(InstanceInfo.InstanceStatus.UP)
                .build();

        application.addInstance(instanceInfo1);
        application.addInstance(instanceInfo2);
        localApps.addApplication(application);
    }

    @Override
    public Applications fetchRegistry() {
        return localApps;
    }

    @Override
    public Applications fetchRegistry(String[] strings) {
        return localApps;
    }
}
