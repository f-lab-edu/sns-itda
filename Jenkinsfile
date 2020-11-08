node ('master') {
    stage('Poll') {
        checkout scm
    }

	stage('Build & Unit test') {
	    sh 'mvn clean verify -DskipITs=true';
	    junit '**/target/surefire-reports/Test-*.xml'
        archive 'target/*.jar'
	}

	stage('Integration test') {
    	    sh 'mvn clean verify -Dsurefire.skip=true';
    	    junit '**/target/surefire-reports/Test-*.xml'
            archive 'target/*.jar'
    }
}
