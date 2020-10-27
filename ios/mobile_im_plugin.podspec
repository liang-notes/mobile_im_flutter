#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html.
# Run `pod lib lint mobile_im_plugin.podspec' to validate before publishing.
#
Pod::Spec.new do |s|
  s.name             = 'mobile_im_plugin'
  s.version          = '0.0.1'
  s.summary          = 'A new free mobile im Flutter plugin.'
  s.description      = <<-DESC
A new free mobile im Flutter plugin.
                       DESC
  s.homepage         = 'http://example.com'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'Your Company' => 'email@example.com' }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/*'
  s.public_header_files = 'Classes/**/*.h', 'Classes/Lib/usr/local/include/*.h'
  s.dependency 'Flutter'
  s.platform = :ios, '8.0'
  s.vendored_libraries = 'Classes/Lib/libMobileIMSDK4iX_common.a'
  s.static_framework = true

  # Flutter.framework does not contain a i386 slice. Only x86_64 simulators are supported.
  s.pod_target_xcconfig = { 'DEFINES_MODULE' => 'NO', 'VALID_ARCHS[sdk=iphonesimulator*]' => 'x86_64' }
end
