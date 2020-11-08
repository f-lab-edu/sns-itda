node ('master') {
    stage('Poll') {
        checkout scm
    }

	stage('Build & Unit test') {
	    sh 'mvn surefire:test'
	    junit '**/target/surefire-reports/Test-*.xml'
        archive 'target/*.jar'
	}

	stage('Integration test') {
    	    sh 'mvn failsafe:integration-test'
    	    junit '**/target/surefire-reports/Test-*.xml'
            archive 'target/*.jar'
    }
}
